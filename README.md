# CodeQL サンプル（Ant 複数 + シェルスクリプト + 独自フレームワーク + JSP）

GitHub CodeQL の `build-mode: manual` を、実案件に近い構成で試すためのサンプル。

## 構成

```
build_all.sh               全体ビルド（ライブラリ取得 → framework → webapp の順に ant を呼ぶ）
pom.xml                    依存ライブラリの宣言専用（Dependabot が読む。ビルドには使わない）
build-libs.xml             pom.xml のバージョンを読み、Maven Central から lib/ に jar を取得
framework/                 独自フレームワーク（*.do → Action のフロントコントローラ）
webapp/
  src/.../action/          業務 Action（意図的な脆弱性入り）
  web/*.jsp                JSP（スクリプトレットに脆弱性入り）
  build.xml                compile → jspc(JSP→Java→javac) → war
.github/workflows/codeql.yml   ubuntu-latest で CodeQL（manual build）+ SBOM 生成
```

## ローカルビルド

前提: JDK 17 以上、Ant 1.10 系が PATH にあること。

```bash
bash build_all.sh   # Windows では Git Bash で実行
```

成果物: `webapp/dist/sample.war`

## 仕込んである脆弱性

### コード（CodeQL で検出）

| 場所 | 種類 | CodeQL ルール |
|---|---|---|
| `UserSearchAction.java` | SQL インジェクション | `java/sql-injection` |
| `FileDownloadAction.java` | パストラバーサル | `java/path-injection` |
| `EchoAction.java` | XSS | `java/xss` |
| `search.jsp` | SQL インジェクション / XSS | 同上（JSP から生成された `search_jsp.java` 上で検出） |
| `SafeUserSearchAction.java` | なし（比較用） | - |

### 依存ライブラリ（Dependabot で検出）

`pom.xml` の Tomcat はあえて既知の脆弱性がある `10.1.0` にしている。

## ポイント

- **CodeQL は `.jsp` を直接解析しない。** `webapp/build.xml` の `jspc` ターゲットで Jasper により JSP を Java に変換し、javac でコンパイルすることで解析対象に入れている。検出位置は JSP ではなく生成された `webapp/build/jsp-src/**/*_jsp.java` になる。
- 独自フレームワークも同じビルド内でソースからコンパイルしているため、`ActionContext.param()` → `request.getParameter()` のようなラッパー越しでもデータフローが追跡される。フレームワークを jar だけで参照すると追跡精度が落ちる可能性がある。
- `init` と `analyze` の間で実際に javac が走ったソースだけが解析される。インクリメンタルビルドにならないよう `clean` してからビルドすること。
- **依存ライブラリは `pom.xml` で宣言する。** Ant は GitHub に依存関係として認識されないため、宣言専用の pom.xml を置いて Dependency graph / Dependabot に読ませている。`build-libs.xml` は `<xmlproperty>` で pom.xml の `<properties>` を読むので、バージョンの管理場所は pom.xml だけになる（Dependabot の更新 PR がそのままビルドに反映される）。
