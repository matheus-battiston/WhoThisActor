import MobilePageHeader from "../../../components/mobile-page-header/mobile-page-header.component";
import ScrollFade from "../../../components/scroll-fade/scroll-fade.component";
import SobreContent from "../../../components/sobre-content/sobre-content.component";
import "./sobre-mobile.css";

export default function SobreMobileLayout() {
  return (
    <div className="sobre-mobile">
      <ScrollFade />

      <MobilePageHeader />

      <main className="sobre-mobile-conteudo">
        <SobreContent />
      </main>
    </div>
  );
}
