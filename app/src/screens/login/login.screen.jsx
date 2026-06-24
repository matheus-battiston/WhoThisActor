import { Background } from "../../components/background/background.component";
import { LoginContainer } from "../../components/login-container/login-container.component";
import { FormLogin } from "../../components/form-login/form-login.component";

export function LoginScreen() {
  return (
    <Background>
      <LoginContainer>
        <FormLogin />
      </LoginContainer>
    </Background>
  );
}
