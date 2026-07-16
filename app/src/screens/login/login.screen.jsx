import { useState } from "react";
import { Background } from "../../components/background/background.component";
import Loading from "../../components/loading/loading.component";
import { LoginContainer } from "../../components/login-container/login-container.component";
import { FormLogin } from "../../components/form-login/form-login.component";

export function LoginScreen() {
  const [loading, setLoading] = useState(false);

  return (
    <Background>
      {loading ? (
        <Loading frase="Logando" />
      ) : (
        <LoginContainer>
          <FormLogin onLoadingChange={setLoading} />
        </LoginContainer>
      )}
    </Background>
  );
}
