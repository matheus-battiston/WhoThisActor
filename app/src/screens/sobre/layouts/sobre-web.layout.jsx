import MobilePageHeader from "../../../components/mobile-page-header/mobile-page-header.component";
import SobreContent from "../../../components/sobre-content/sobre-content.component";
import "./sobre-web.css";

export default function SobreWebLayout() {
  return (
    <div className="sobre-web">
      <MobilePageHeader />

      <main className="sobre-web-conteudo">
        <SobreContent />
      </main>
    </div>
  );
}
