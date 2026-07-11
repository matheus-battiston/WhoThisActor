import CameraAltOutlinedIcon from "@mui/icons-material/CameraAltOutlined";
import ManageSearchOutlinedIcon from "@mui/icons-material/ManageSearchOutlined";
import "./opcoes-atores-ajuda.css";

const dicas = [
  {
    titulo: "Para reconhecer pela camera",
    Icone: CameraAltOutlinedIcon,
    itens: [
      "Prefira um rosto de frente ou levemente virado, com boa luz e pouca sombra.",
      "Evite imagens muito borradas, muito distantes ou com varios rostos em destaque.",
      "Quando possivel, use um frame em que olhos, nariz e boca estejam visiveis.",
    ],
  },
  {
    titulo: "Para buscar pelo nome",
    Icone: ManageSearchOutlinedIcon,
    itens: [
      "Pesquise pelo nome artistico mais conhecido, sem apelidos ou nomes de personagem.",
      "Se souber, inclua nome e sobrenome para reduzir resultados parecidos.",
      "Confira acentos e grafias alternativas quando o ator tiver nome internacional.",
    ],
  },
];

export default function OpcoesAtoresAjuda() {
  return (
    <section className="opcoes-atores-ajuda">
      <div className="opcoes-atores-ajuda-cabecalho">
        <span className="opcoes-atores-ajuda-etiqueta">Nao era nenhum deles?</span>
        <h2>Nao encontramos quem voce estava procurando?</h2>
        <p>
          Algumas buscas ficam ambiguas quando a foto ou o nome combinam com
          mais de uma pessoa. Estas dicas ajudam a refinar o resultado.
        </p>
      </div>

      <div className="opcoes-atores-ajuda-grid">
        {dicas.map(({ titulo, Icone, itens }) => (
          <article key={titulo} className="opcoes-atores-ajuda-card">
            <div className="opcoes-atores-ajuda-card-titulo">
              <Icone aria-hidden="true" />
              <h3>{titulo}</h3>
            </div>

            <ul>
              {itens.map((item) => (
                <li key={item}>{item}</li>
              ))}
            </ul>
          </article>
        ))}
      </div>
    </section>
  );
}
