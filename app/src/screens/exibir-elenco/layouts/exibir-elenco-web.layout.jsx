import ElencoWebCard from "../../../components/elenco-web-card/elenco-web-card.component";
import InputSearch from "../../../components/pesquisa/pesquisa.component";
import ProducaoWebHero from "../../../components/producao-web-hero/producao-web-hero.component";
import "./exibir-elenco-web.css";

export default function ExibirElencoWebLayout({
  detalhesProducao,
  elenco,
  onPesquisarPersonagem,
  isAuthenticated,
  favoritado,
  favoritoPendente,
  favoritar,
}) {
  return (
    <div className="exibir-elenco-web">
      <ProducaoWebHero
        producao={detalhesProducao}
        isAuthenticated={isAuthenticated}
        favoritado={favoritado}
        favoritoPendente={favoritoPendente}
        favoritar={favoritar}
      />

      <main className="exibir-elenco-web-conteudo">
        <InputSearch
          onChange={onPesquisarPersonagem}
          placeholder="Buscar no elenco..."
          wrapperClassName="pesquisa-web-elenco"
          inputClassName="input-web-elenco"
          mostrarIcone
          ariaLabel="Buscar no elenco"
        />

        <div className="exibir-elenco-web-lista-personagens">
          {elenco.map((item) => (
            <ElencoWebCard key={item.id} pessoa={item} />
          ))}
        </div>
      </main>
    </div>
  );
}
