import BotaoVoltar from "../back-button/back-button.component";
import HeaderLogoButton from "../header-logo-button/header-logo-button.component";
import "./mobile-page-header.css";

export default function MobilePageHeader() {
  return (
    <header className="mobile-page-header">
      <BotaoVoltar className="mobile-page-header-back" />

      <HeaderLogoButton />
    </header>
  );
}
