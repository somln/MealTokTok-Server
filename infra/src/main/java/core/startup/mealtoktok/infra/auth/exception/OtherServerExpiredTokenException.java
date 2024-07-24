package core.startup.mealtoktok.infra.auth.exception;

import core.startup.mealtoktok.common.exception.InfraException;

public class OtherServerExpiredTokenException extends InfraException {

    public static final InfraException EXCEPTION = new OtherServerExpiredTokenException();

    private OtherServerExpiredTokenException() {
        super(AuthErrorCode.OTHER_SERVER_EXPIRED_TOKEN);
    }
}