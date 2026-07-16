import OpcaoProducaoWebCard from "../../../components/opcao-producao-web-card/opcao-producao-web-card.component";
import ResultadoPesquisaOpcao from "../../../components/search-result-intro/search-result-intro.component";
import "./opcoes-producoes-web.css";

export default function OpcoesProducoesWebLayout({
  opcoes,
  tipoMidia,
  abrirProducao,
  introTitle,
  introSubtitle,
}) {
  return (
    <div className="opcoes-producoes-web">
      <main className="opcoes-producoes-web-conteudo">
        <ResultadoPesquisaOpcao title={introTitle} subtitle={introSubtitle} />

        <div className="opcoes-producoes-web-lista">
          {opcoes.map((item, index) => (
            <OpcaoProducaoWebCard
              key={`${item.id}-${index}`}
              producao={item}
              tipoMidia={tipoMidia}
              onClick={() => abrirProducao(item)}
            />
          ))}
        </div>
      </main>
    </div>
  );
}
