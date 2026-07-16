import AtorWebCard from "../../../components/ator-web-card/ator-web-card.component";
import MediaTypeToggle from "../../../components/media-type-toggle/media-type-toggle.component";
import ProducaoAtorWebCard from "../../../components/producao-ator-web-card/producao-ator-web-card.component";
import "./exibir-producoes-web.css";

function obterProducoes(ator, tab) {
  if (tab === "FILME") return ator.producoes?.filmes || [];

  return ator.producoes?.series || [];
}

export default function ExibirProducoesWebLayout({
  ator,
  tab,
  setTab,
  isAuthenticated,
  favoritado,
  favoritoPendente,
  favoritar,
}) {
  const producoes = obterProducoes(ator, tab);

  return (
    <div className="exibir-producoes-web">
      <main className="exibir-producoes-web-conteudo">
        <AtorWebCard
          nome={ator.nome}
          imagem={ator.urlFoto}
          logado={isAuthenticated}
          favoritado={favoritado}
          favoritar={favoritar}
          favoritoPendente={favoritoPendente}
        />

        <MediaTypeToggle
          value={tab}
          onChange={setTab}
          className="exibir-producoes-web-tabs"
        />

        <div className="exibir-producoes-web-lista">
          {producoes.map((producao) => (
            <ProducaoAtorWebCard
              key={`${tab}-${producao.id}-${producao.nomePersonagem}`}
              producao={producao}
              tipo={tab}
            />
          ))}
        </div>
      </main>
    </div>
  );
}
