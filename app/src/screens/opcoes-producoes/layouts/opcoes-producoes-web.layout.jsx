import ExibeProducao from "../../../components/exibe-producao/exibe-producao.component";
import MobilePageHeader from "../../../components/mobile-page-header/mobile-page-header.component";
import "./opcoes-producoes-web.css";

export default function OpcoesProducoesWebLayout({
  opcoes,
  mapearProducao,
  mapearTipo,
}) {
  return (
    <div className="opcoes-producoes-web">
      <MobilePageHeader />

      <div className="opcoes-producoes-web-lista">
        {opcoes.map((item, index) => (
          <ExibeProducao
            key={`${item.id}-${index}`}
            producao={mapearProducao(item)}
            tipo={mapearTipo(item)}
          />
        ))}
      </div>
    </div>
  );
}
