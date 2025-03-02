import "./botao.css";

const BotaoComponent = ({ onPress, texto }) => {
  return (
    <button className="button" onClick={onPress}>
      <span className="button-text">{texto}</span>
    </button>
  );
};

export default BotaoComponent;
