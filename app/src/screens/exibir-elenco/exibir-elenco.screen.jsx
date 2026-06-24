import { useParams } from "react-router-dom";
import { useNomePersonagem } from "../../hooks/use-nome-personagem/use-nome-personagem.hook";
import Loading from "../../components/loading/loading.component";
import SerieInfo from "../../components/serie-info/serie-info.component";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/image-tmdb";
import InputSearch from "../../components/pesquisa/pesquisa.component";
import "./exibir-elenco.css";
import ExibePessoaPersonagem from "../../components/exibe-pessoa-personagem/exibe-pessoa-personagem.component";
import Cabecalho from "../../components/cabecalho/cabecalho.component";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import { useFavoritoProducao } from "../../api/hooks/use-favorito-producao/use-favorito-producao.hook";
import {
  usePesquisarDetalhesFilmeComElenco,
  usePesquisarDetalhesSerieComElenco,
} from "../../api/generated/api";

export function ExibirElencoScreen() {
  const { changePersonagem, personagemNome } = useNomePersonagem();
  const { tipo, id } = useParams();

  const idProducao = Number(id);
  const isFilme = tipo === "MOVIE";

  const { isAuthenticated, authChecked } = useAuth();

  const FRASE_CARREGAMENTO = "Carregando elenco";
  const PLACEHOLDER = "Alex Dunphy";

  const parametrosPesquisa = {
    personagem: personagemNome || undefined,
  };

  const detalhesSerie = usePesquisarDetalhesSerieComElenco(
    idProducao,
    parametrosPesquisa,
    {
      query: {
        enabled: !isFilme && Boolean(idProducao),
        placeholderData: (previousData) => previousData,
      },
    },
  );

  const detalhesFilme = usePesquisarDetalhesFilmeComElenco(
    idProducao,
    parametrosPesquisa,
    {
      query: {
        enabled: isFilme && Boolean(idProducao),
        placeholderData: (previousData) => previousData,
      },
    },
  );

  const detalhesQuery = isFilme ? detalhesFilme : detalhesSerie;

  const detalhesProducao = detalhesQuery.data?.data;

  const { favoritoLocal, favoritoPendente, alternarFavorito } =
    useFavoritoProducao(
      detalhesProducao?.id,
      tipo,
      isAuthenticated,
      detalhesProducao?.estaFavoritado,
    );

  const carregamentoInicial =
    !authChecked || detalhesQuery.isLoading || detalhesProducao == null;

  return (
    <div className="container-elenco">
      {carregamentoInicial ? (
        <Loading frase={FRASE_CARREGAMENTO} />
      ) : (
        <div className="conteudo-elenco">
          <Cabecalho />

          <div className="serie-info-input">
            <SerieInfo
              imagem={URL_BASE_IMAGEM_TMDB + detalhesProducao.imagem}
              nome={detalhesProducao.nome}
              providers={detalhesProducao.providers}
              isAuthenticated={isAuthenticated}
              favoritado={favoritoLocal}
              favoritoPendente={favoritoPendente}
              favoritar={alternarFavorito}
            />

            <div className="input-personagem">
              <InputSearch
                onChange={changePersonagem}
                texto={"Pesquise seu personagem"}
                placeholder={PLACEHOLDER}
              />
            </div>
          </div>

          <div className="lista-personagens">
            {detalhesProducao.elenco.map((item) => (
              <ExibePessoaPersonagem key={item.id} pessoa={item} />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}