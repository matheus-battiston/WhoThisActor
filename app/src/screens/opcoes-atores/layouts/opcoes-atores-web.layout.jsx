import PessoaWebCard from "../../../components/pessoa-web-card/pessoa-web-card.component";
import OpcoesAtoresAjuda from "../../../components/opcoes-atores-ajuda/opcoes-atores-ajuda.component";
import ResultadoPesquisaOpcao from "../../../components/search-result-intro/search-result-intro.component";
import { URL_BASE_IMAGEM_TMDB } from "../../../constants/image-tmdb";
import "./opcoes-atores-web.css";

function montarImagem(path) {
  if (!path) return undefined;
  if (path.startsWith("http")) return path;

  return `${URL_BASE_IMAGEM_TMDB}${path}`;
}

export default function OpcoesAtoresWebLayout({
  opcoes,
  abrirAtor,
  introTitle,
  introSubtitle,
}) {
  return (
    <div className="opcoes-atores-web">
      <main className="opcoes-atores-web-conteudo">
        <ResultadoPesquisaOpcao title={introTitle} subtitle={introSubtitle} />

        <div className="opcoes-atores-web-lista">
          {opcoes.map((ator, index) => (
            <PessoaWebCard
              key={`${ator.id}-${index}`}
              nome={ator.identidade}
              imagem={montarImagem(ator.imagem)}
              onClick={() => abrirAtor(ator)}
            />
          ))}
        </div>

        <OpcoesAtoresAjuda />
      </main>
    </div>
  );
}
