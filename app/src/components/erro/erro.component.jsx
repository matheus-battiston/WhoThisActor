import { useDispatch, useSelector } from "react-redux";
import { clearError } from "../../redux/store"; // Ajuste o caminho conforme necessário

import "./erro.css";
import BotaoComponent from "../botao/botao.component";

export default function Erro() {
  const dispatch = useDispatch();
  const { message } = useSelector((state) => state.error); // Acessa o erro no Redux

  function onClose() {
    dispatch(clearError());
  }

  return (
    <div className="modal-overlay">
      <div className="modal">
        <p>{message}</p>
        <BotaoComponent onPress={onClose} texto={"Continuar"} />
      </div>
    </div>
  );
}
