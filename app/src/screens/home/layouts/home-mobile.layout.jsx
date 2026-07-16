import AuthActionButton from "../../../components/auth-action-button/auth-action-button.component";
import CameraActionCard from "../../../components/camera-action-card/camera-action-card.component";
import Logo from "../../../components/logo/logo.component";
import HomeLoading from "../../../components/home-loading/home-loading.component";
import "./home-mobile.css";

export default function HomeMobileLayout({ authChecked }) {
  return (
    <div className="home-mobile">
      <Logo
        className={!authChecked ? "logo-image-loading" : "logo-image-ready"}
      />

      {!authChecked ? (
        <HomeLoading />
      ) : (
        <main className="home-mobile-conteudo">
          <section className="home-mobile-intro">
            <span>Reconhecimento por imagem</span>
            <h1>Descubra quem é o ator e onde você já o viu.</h1>
            <AuthActionButton className="home-mobile-auth-botao" />
          </section>

          <CameraActionCard />
        </main>
      )}
    </div>
  );
}
