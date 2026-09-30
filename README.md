# CodeQL サンプル（Ant 複数 + バッチ + 独自フレームワーク + JSP）

GitHub CodeQL の `build-mode: manual` を、実案件に近い構成で試すためのサンプル。

## 構成

```
build_all.bat              全体ビルド（ライブラリ取得 → framework → webapp の順に ant を呼ぶ）
build-libs.xml             Maven Central から Tomcat(Jasper) 等を lib/ に取得
framework/                 独自フレームワーク（*.do → Action のフロントコントローラ）
webapp/
  src/.../action/          業務 Action（意図的な脆弱性入り）
  web/*.jsp                JSP（スクリプトレットに脆弱性入り）
  build.xml                compile → jspc(JSP→Java→javac) → war
.github/workflows/codeql.yml
```

## ローカルビルド

前提: JDK 17 以上、Ant 1.10 系が PATH にあること。

```bat
build_all.bat
```

成果物: `webapp/dist/sample.war`

## 仕込んである脆弱性（CodeQL で検出される想定）

| 場所 | 種類 | CodeQL ルール |
|---|---|---|
| `UserSearchAction.java` | SQL インジェクション | `java/sql-injection` |
| `FileDownloadAction.java` | パストラバーサル | `java/path-injection` |
| `EchoAction.java` | XSS | `java/xss` |
| `search.jsp` | SQL インジェクション / XSS | 同上（JSP から生成された `search_jsp.java` 上で検出） |
| `SafeUserSearchAction.java` | なし（比較用） | - |

## ポイント

- **CodeQL は `.jsp` を直接解析しない。** `webapp/build.xml` の `jspc` ターゲットで Jasper により JSP を Java に変換し、javac でコンパイルすることで解析対象に入れている。検出位置は JSP ではなく生成された `webapp/build/jsp-src/**/*_jsp.java` になる。
- 独自フレームワークも同じビルド内でソースからコンパイルしているため、`ActionContext.param()` → `request.getParameter()` のようなラッパー越しでもデータフローが追跡される。フレームワークを jar だけで参照すると追跡精度が落ちる可能性がある。
- `init` と `analyze` の間で実際に javac が走ったソースだけが解析される。インクリメンタルビルドにならないよう `clean` してからビルドすること。
