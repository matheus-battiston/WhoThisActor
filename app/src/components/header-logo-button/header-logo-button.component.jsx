import { useNavigate } from "react-router-dom";
import logoImage from "../../assets/images/WhoThisActorLogoNoText.png";
import "./header-logo-button.css";

export default function HeaderLogoButton({ className = "" }) {
  const navigate = useNavigate();

  return (
    <button
      type="button"
      className={["header-logo-button", className].filter(Boolean).join(" ")}
      onClick={() => navigate("/")}
      aria-label="Ir para a home"
    >
      <img src={logoImage} alt="WhoThis" />
      <strong>
        Who<span>This</span>
      </strong>
    </button>
  );
}
