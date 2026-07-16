import MovieOutlinedIcon from "@mui/icons-material/MovieOutlined";
import TvOutlinedIcon from "@mui/icons-material/TvOutlined";
import "./tipo-producao-toggle.css";

export default function TipoProducaoToggle({ tipoProducao, onChange }) {
  const proximoTipo = tipoProducao === "filmes" ? "series" : "filmes";
  const Icone = tipoProducao === "filmes" ? TvOutlinedIcon : MovieOutlinedIcon;

  return (
    <button
      type="button"
      className="tipo-producao-toggle"
      onClick={() => onChange(proximoTipo)}
      aria-label={
        proximoTipo === "filmes"
          ? "Ver filmes favoritos"
          : "Ver séries favoritas"
      }
    >
      <Icone />
    </button>
  );
}
