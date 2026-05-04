import { useNavigate } from "react-router-dom";
import "./cabecalho.css";
import logoImage from "../../assets/images/WhoThisActorLogoNoText.png";

export default function Cabecalho() {
  const navigate = useNavigate();

  function Home() {
    navigate("/");
  }

  return (
    <div className="cabecalho">
      <img
        src={logoImage}
        alt="Logo"
        className="logo-image-cabecalho"
        onClick={Home}
      />
    </div>
  );
}
