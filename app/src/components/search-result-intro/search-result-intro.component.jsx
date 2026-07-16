import SearchOutlinedIcon from "@mui/icons-material/SearchOutlined";
import "./search-result-intro.css";

export default function ResultadoPesquisaOpcao({ title, subtitle }) {
  return (
    <section className="resultado-pesquisa-intro">
      <div className="resultado-pesquisa-intro-icon">
        <SearchOutlinedIcon />
      </div>

      <div className="resultado-pesquisa-intro-texto">
        <h1>{title}</h1>
        {subtitle ? <p>{subtitle}</p> : null}
      </div>
    </section>
  );
}
