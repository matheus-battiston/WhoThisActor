import OpcaoAtorCard from "../../../components/opcao-ator-card/opcao-ator-card.component";
import OpcoesAtoresAjuda from "../../../components/opcoes-atores-ajuda/opcoes-atores-ajuda.component";
import MobilePageHeader from "../../../components/mobile-page-header/mobile-page-header.component";
import ScrollFade from "../../../components/scroll-fade/scroll-fade.component";
import ResultadoPesquisaOpcao from "../../../components/search-result-intro/search-result-intro.component";
import "./opcoes-atores-mobile.css";

export default function OpcoesAtoresMobileLayout({
  opcoes,
  abrirAtor,
  introTitle,
  introSubtitle,
}) {
  return (
    <div className="opcoes-atores-mobile">
      <ScrollFade />

      <MobilePageHeader />

      <main className="opcoes-atores-mobile-conteudo">
        <ResultadoPesquisaOpcao
          title={introTitle}
          subtitle={introSubtitle}
        />

        <div className="opcoes-atores-mobile-lista">
          {opcoes.map((ator, index) => (
            <OpcaoAtorCard
              key={`${ator.id}-${index}`}
              ator={ator}
              onClick={() => abrirAtor(ator)}
            />
          ))}
        </div>

        <OpcoesAtoresAjuda />
      </main>
    </div>
  );
}
