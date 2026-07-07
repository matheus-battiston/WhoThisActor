import AtorInfo from "../../../components/ator-info/ator-info.component";
import MobilePageHeader from "../../../components/mobile-page-header/mobile-page-header.component";
import "./opcoes-atores-web.css";

export default function OpcoesAtoresWebLayout({ opcoes, abrirAtor }) {
  return (
    <div className="opcoes-atores-web">
      <MobilePageHeader />

      <div className="opcoes-atores-web-lista">
        {opcoes.map((ator, index) => (
          <button
            key={`${ator.identidade}-${index}`}
            type="button"
            className="opcoes-atores-web-item"
            onClick={() => abrirAtor(ator)}
          >
            <AtorInfo nome={ator.identidade} imagem={ator.imagem} />
          </button>
        ))}
      </div>
    </div>
  );
}
