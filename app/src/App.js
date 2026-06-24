import "./App.css";
import { RouterProvider } from "react-router-dom";
import { router } from "./router";
import { Provider, useSelector } from "react-redux";
import { store } from "./redux/store";
import "./index.css";
import { useBootstrapAuth } from "./api/hooks/use-bootstrap/use-bootstrap.hook";
import Erro from "./components/erro/erro.component";

function AppContent() {
  const { hasError } = useSelector((state) => state.error);

  useBootstrapAuth();

  return (
    <>
      {hasError ? <Erro /> : null}
      <RouterProvider router={router} />
    </>
  );
}

function App() {
  return (
    <Provider store={store}>
      <AppContent />
    </Provider>
  );
}

export default App;
