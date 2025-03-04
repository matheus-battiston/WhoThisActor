import { useNavigate } from "react-router-dom";
import "./cabecalho.css";
import logoImage from "../../assets/images/favicon.ico";

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
      <h1 className="titulo">Who This Actor</h1>
    </div>
  );
}
