import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./formLogin.css";
import { GoogleButtonCustom } from "../google-button/google-button.component";
import Loading from "../loading/loading.component";

export function FormLogin() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const FRASE_CARREGAENTO = "Logando";

  return loading ? (
    <Loading frase={FRASE_CARREGAENTO} />
  ) : (
    <div className="login-content">
      <button
        className="login-back-button"
        onClick={() => navigate("/")}
        type="button"
      >
        ← Voltar ao menu
      </button>

      <div className="login-badge">Who This Actor</div>

      <div className="login-copy">
        <p className="login-eyebrow">
          Sua busca de atores, filmes e series em um so lugar
        </p>
        <h1 className="login-title">
          Descubra rostos, elencos e producoes com facilidade.
        </h1>
        <p className="login-subtitle">
          Entre com Google para salvar favoritos, continuar sua jornada e
          acessar tudo em segundos.
        </p>
      </div>

      <div className="login-google-section">
        <GoogleButtonCustom setLoading={setLoading} />
      </div>

      <div className="login-note">
        Login unico com Google para uma experiencia mais rapida e sem senha.
      </div>
    </div>
  );
}
