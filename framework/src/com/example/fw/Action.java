package com.example.fw;

/**
 * 業務処理の基本インタフェース。
 * 戻り値が "forward:/xxx.jsp" なら JSP へフォワード、null ならレスポンスは書き込み済み。
 */
public interface Action {
    String execute(ActionContext ctx) throws Exception;
}
