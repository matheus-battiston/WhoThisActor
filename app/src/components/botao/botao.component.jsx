import "./botao.css";

const BotaoComponent = ({ onPress, texto, big = false }) => {
  return (
    <button className={`button ${big ? "big" : ""}`} onClick={onPress}>
      <span className="button-text">{texto}</span>
    </button>
  );
};

export default BotaoComponent;
