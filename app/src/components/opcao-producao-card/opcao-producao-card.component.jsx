import { obterAnoLancamento } from "../../utils/formatar-data-lancamento";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./opcao-producao-card.css";

function montarImagem(path) {
  return path || undefined;
}

function montarPeriodo(producao, tipoMidia) {
  if (tipoMidia === "MOVIE") return obterAnoLancamento(producao.dataLancamento);

  const anoInicial = producao.anoPrimeiraTemporada;
  const anoFinal = producao.anoUltimaTemporada;

  if (anoInicial && anoFinal) return `${anoInicial} - ${anoFinal}`;
  if (anoInicial) return `${anoInicial} - Atual`;

  return null;
}

export default function OpcaoProducaoCard({ producao, tipoMidia, onClick }) {
  const tipo = producao.tipoMidia || tipoMidia;
  const periodo = montarPeriodo(producao, tipo);

  return (
    <button type="button" className="opcao-producao-card" onClick={onClick}>
      <LoadingImage
        src={montarImagem(producao.imagem)}
        alt={producao.nome}
        className="opcao-producao-card-imagem"
        imgClassName="opcao-producao-card-img"
      />

      <span className="opcao-producao-card-conteudo">
        <strong>{producao.nome}</strong>

        {periodo ? (
          <span className="opcao-producao-card-periodo">{periodo}</span>
        ) : null}

        {producao.genero ? (
          <span className="opcao-producao-card-genero">{producao.genero}</span>
        ) : null}

        {producao.overview ? (
          <span className="opcao-producao-card-overview">
            {producao.overview}
          </span>
        ) : null}
      </span>
    </button>
  );
}
