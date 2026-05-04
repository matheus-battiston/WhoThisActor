import { useDispatch, useSelector } from "react-redux";
import { clearError } from "../../redux/store";

import "./erro.css";
import BotaoComponent from "../botao/botao.component";

export default function Erro({ message }) {
  const dispatch = useDispatch();
  const { message: globalMessage } = useSelector((state) => state.error);

  function onClose() {
    dispatch(clearError());
  }

  return (
    <div className="modal-overlay">
      <div className="modal">
        <p>{message ?? globalMessage}</p>
        <BotaoComponent onPress={onClose} texto={"Continuar"} />
      </div>
    </div>
  );
}
