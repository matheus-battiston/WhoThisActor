import tmdbLogo from "../../assets/images/tmdb-logo.svg";
import "./sobre-content.css";

export default function SobreContent() {
  return (
    <>
      <section className="sobre-hero">
        <span>Sobre o WhoThis</span>
        <h1>Reconhecimento de atores com contexto de filmes e séries.</h1>
        <p>
          O WhoThis ajuda você a identificar atores a partir de uma imagem ou
          pesquisa e a navegar rapidamente pelas produções em que eles
          participaram.
        </p>
      </section>

      <section className="sobre-card">
        <div className="sobre-card-cabecalho">
          <span>Dados de entretenimento</span>
          <img src={tmdbLogo} alt="The Movie Database (TMDB)" />
        </div>

        <p>
          As informações sobre atores, filmes, séries, imagens, sinopses e
          metadados exibidos no app são fornecidas pela API do TMDB.
        </p>

        <p className="sobre-aviso-tmdb">
          This product uses the TMDB API but is not endorsed or certified by
          TMDB.
        </p>
      </section>

      <section className="sobre-card">
        <span>Reconhecimento e privacidade</span>
        <p>
          A tecnologia de reconhecimento facial utilizada para classificar as
          imagens é proprietária do WhoThis. As imagens enviadas são usadas
          apenas para o processo de classificação.
        </p>
        <p>
          Eventualmente, informações extraídas da imagem poderão ser utilizadas
          para melhorar o modelo. A foto em si não será salva: apenas dados
          técnicos derivados dela poderão ser mantidos.
        </p>
        <p>
          Nenhuma pessoa terá acesso à foto enviada, incluindo o desenvolvedor
          do app.
        </p>
      </section>
    </>
  );
}
