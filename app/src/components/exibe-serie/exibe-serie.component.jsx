import "./exibe-serie.css";

export default function ExibeSerie({ nome, imagem }) {
  return (
    <div className="linha-serie">
      <img src={imagem} alt={nome} className="poster-serie" />
      <div className="texto-producao">
        <h2 className="titulo-producao">{nome}</h2>
      </div>
    </div>
  );
}
