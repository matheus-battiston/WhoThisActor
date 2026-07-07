import { useNavigate } from "react-router-dom";
import logoImage from "../../assets/images/WhoThisActorLogoNoText.png";
import BotaoVoltar from "../back-button/back-button.component";
import "./mobile-page-header.css";

export default function MobilePageHeader() {
  const navigate = useNavigate();

  return (
    <header className="mobile-page-header">
      <BotaoVoltar className="mobile-page-header-back" />

      <button
        type="button"
        className="mobile-page-header-logo"
        onClick={() => navigate("/")}
        aria-label="Ir para a home"
      >
        <img src={logoImage} alt="WhoThis" />
        <strong>
          Who<span>This</span>
        </strong>
      </button>
    </header>
  );
}
