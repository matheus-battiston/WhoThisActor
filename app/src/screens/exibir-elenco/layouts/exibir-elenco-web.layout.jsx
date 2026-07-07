import Cabecalho from "../../../components/cabecalho/cabecalho.component";
import ExibePessoaPersonagem from "../../../components/exibe-pessoa-personagem/exibe-pessoa-personagem.component";
import InputSearch from "../../../components/pesquisa/pesquisa.component";
import SerieInfo from "../../../components/serie-info/serie-info.component";
import { URL_BASE_IMAGEM_TMDB } from "../../../constants/image-tmdb";
import "./exibir-elenco-web.css";

export default function ExibirElencoWebLayout({
  detalhesProducao,
  elenco,
  onPesquisarPersonagem,
  placeholderPesquisa,
  isAuthenticated,
  favoritado,
  favoritoPendente,
  favoritar,
}) {
  return (
    <div className="exibir-elenco-web">
      <Cabecalho />

      <div className="exibir-elenco-web-serie-info-input">
        <SerieInfo
          imagem={URL_BASE_IMAGEM_TMDB + detalhesProducao.imagem}
          nome={detalhesProducao.nome}
          providers={detalhesProducao.providers}
          isAuthenticated={isAuthenticated}
          favoritado={favoritado}
          favoritoPendente={favoritoPendente}
          favoritar={favoritar}
        />

        <div className="exibir-elenco-web-input-personagem">
          <InputSearch
            onChange={onPesquisarPersonagem}
            texto={"Pesquise seu personagem"}
            placeholder={placeholderPesquisa}
          />
        </div>
      </div>

      <div className="exibir-elenco-web-lista-personagens">
        {elenco.map((item) => (
          <ExibePessoaPersonagem key={item.id} pessoa={item} />
        ))}
      </div>
    </div>
  );
}
