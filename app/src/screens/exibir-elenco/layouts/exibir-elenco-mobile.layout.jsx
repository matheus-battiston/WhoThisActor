import InputSearch from "../../../components/pesquisa/pesquisa.component";
import ElencoMobileCard from "../../../components/elenco-mobile-card/elenco-mobile-card.component";
import ProducaoMobileHero from "../../../components/producao-mobile-hero/producao-mobile-hero.component";
import "./exibir-elenco-mobile.css";

export default function ExibirElencoMobileLayout({
  detalhesProducao,
  elenco,
  onPesquisarPersonagem,
  isAuthenticated,
  favoritado,
  favoritoPendente,
  favoritar,
}) {
  return (
    <div className="exibir-elenco-mobile">
      <ProducaoMobileHero
        producao={detalhesProducao}
        isAuthenticated={isAuthenticated}
        favoritado={favoritado}
        favoritoPendente={favoritoPendente}
        favoritar={favoritar}
      />

      <main className="exibir-elenco-mobile-conteudo">
        <InputSearch
          onChange={onPesquisarPersonagem}
          placeholder="Buscar no elenco..."
          wrapperClassName="pesquisa-mobile-elenco"
          inputClassName="input-mobile-elenco"
          mostrarIcone
          ariaLabel="Buscar no elenco"
        />

        <div className="lista-personagens-mobile">
          {elenco.map((item) => (
            <ElencoMobileCard key={item.id} pessoa={item} />
          ))}
        </div>
      </main>
    </div>
  );
}
