import tmdbLogo from "../../assets/images/tmdb-logo.svg";
import "./sobre-content.css";

const passos = [
  {
    titulo: "Envie ou pesquise",
    texto: "Use uma foto bem iluminada, frontal e com o rosto em evidência para melhorar a precisão. Você também pode buscar por nome.",
  },
  {
    titulo: "Escolha o resultado",
    texto: "Quando houver mais de uma opção, selecione o ator, filme ou série correta.",
  },
  {
    titulo: "Explore o contexto",
    texto: "Veja elenco, filmografia, personagens e salve seus favoritos.",
  },
  {
    titulo: "Opcional: use favoritos",
    texto: "Ao fazer login, você pode favoritar filmes e séries para navegar mais rápido e filtrar a classificação para esse universo, ajudando o modelo a encontrar a melhor resposta.",
  },
];

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

      <section className="sobre-fluxo">
        <div className="sobre-secao-titulo">
          <span>Como funciona</span>
          <h2>Da dúvida ao contexto em poucos passos.</h2>
        </div>

        <div className="sobre-passos">
          {passos.map((passo, index) => (
            <article className="sobre-passo" key={passo.titulo}>
              <strong>{String(index + 1).padStart(2, "0")}</strong>
              <h3>{passo.titulo}</h3>
              <p>{passo.texto}</p>
            </article>
          ))}
        </div>
      </section>

      <div className="sobre-cards">
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
            Eventualmente, informações extraídas da imagem poderão ser
            utilizadas para melhorar o modelo. A foto em si não será salva:
            apenas dados técnicos derivados dela poderão ser mantidos.
          </p>
          <p>
            Nenhuma pessoa terá acesso à foto enviada, incluindo o desenvolvedor
            do app.
          </p>
        </section>
      </div>
    </>
  );
}
