package umc.precending.util;

public enum MessageProvider {
    MESSAGE_DEFAULT_INTRODUCTION("제가 한 선행을 기록으로 남기고 싶어서 가입하게 되었습니다!")
    ;
    private String message;

    MessageProvider(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
