import MobilePageHeader from "../../../components/mobile-page-header/mobile-page-header.component";
import OpcaoProducaoCard from "../../../components/opcao-producao-card/opcao-producao-card.component";
import ScrollFade from "../../../components/scroll-fade/scroll-fade.component";
import ResultadoPesquisaOpcao from "../../../components/search-result-intro/search-result-intro.component";
import "./opcoes-producoes-mobile.css";

export default function OpcoesProducoesMobileLayout({
  opcoes,
  tipoMidia,
  abrirProducao,
  introTitle,
  introSubtitle,
}) {
  return (
    <div className="opcoes-producoes-mobile">
      <ScrollFade />

      <MobilePageHeader />

      <main className="opcoes-producoes-mobile-conteudo">
        <ResultadoPesquisaOpcao title={introTitle} subtitle={introSubtitle} />

        <div className="opcoes-producoes-mobile-lista">
          {opcoes.map((item, index) => (
            <OpcaoProducaoCard
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
