import MovieOutlinedIcon from "@mui/icons-material/MovieOutlined";
import TvOutlinedIcon from "@mui/icons-material/TvOutlined";
import AtorMobileCard from "../../../components/ator-mobile-card/ator-mobile-card.component";
import MobilePageHeader from "../../../components/mobile-page-header/mobile-page-header.component";
import SegmentedControl from "../../../components/segmented-control/segmented-control.component";
import ProducaoMobileCard from "../../../components/producao-mobile-card/producao-mobile-card.component";
import ScrollFade from "../../../components/scroll-fade/scroll-fade.component";
import "./exibir-producoes-mobile.css";

const TABS = [
  { label: "Filmes", value: "FILME", Icone: MovieOutlinedIcon },
  { label: "Séries", value: "TV", Icone: TvOutlinedIcon },
];

function obterProducoes(ator, tab) {
  if (tab === "FILME") return ator.producoes?.filmes || [];

  return ator.producoes?.series || [];
}

export default function ExibirProducoesMobileLayout({
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
    <div className="exibir-producoes-mobile">
      <ScrollFade />

      <main className="exibir-producoes-mobile-conteudo">
        <MobilePageHeader />

        <AtorMobileCard
          nome={ator.nome}
          imagem={ator.urlFoto}
          logado={isAuthenticated}
          favoritado={favoritado}
          favoritoPendente={favoritoPendente}
          favoritar={favoritar}
        />

        <SegmentedControl
          options={TABS}
          value={tab}
          onChange={setTab}
          size="compact"
          className="exibir-producoes-mobile-tabs"
        />

        <div className="exibir-producoes-mobile-lista">
          {producoes.map((producao) => (
            <ProducaoMobileCard
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
