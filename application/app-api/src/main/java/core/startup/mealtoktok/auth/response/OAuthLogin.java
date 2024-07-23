package core.startup.mealtoktok.auth.response;

public record OAuthLogin(
        String link
) {
    public static OAuthLogin from(String link) {
        return new OAuthLogin(link);
    }
}
