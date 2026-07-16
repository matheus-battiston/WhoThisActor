import { createBrowserRouter } from "react-router-dom";
import {
  BuscarScreen,
  ExibirElencoScreen,
  ExibirProducoesScreen,
  HomeScreen,
  OpcoesAtoresScreen,
  OpcoesProducoesScreen,
  SobreScreen,
} from "../screens/index";
import { LoginScreen } from "../screens/login/login.screen";
import FavoritosScreen from "../screens/favoritos/favoritos.screen";
import { PrivateRoute } from "./private-route.component";
import AppShell from "../components/app-shell/app-shell.component";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <AppShell />,
    children: [
      {
        index: true,
        element: <HomeScreen />,
      },
      {
        path: "buscar",
        element: <BuscarScreen />,
      },
      {
        path: "ator/:id",
        element: <ExibirProducoesScreen />,
      },
      {
        path: "opcoesAtores",
        element: <OpcoesAtoresScreen />,
      },
      {
        path: "opcoesProducoes",
        element: <OpcoesProducoesScreen />,
      },
      {
        path: "exibirElenco/:tipo/:id",
        element: <ExibirElencoScreen />,
      },
      {
        path: "fav",
        element: <PrivateRoute Screen={FavoritosScreen} />,
      },
      {
        path: "sobre",
        element: <SobreScreen />,
      },
    ],
  },
  {
    path: "/login",
    element: <LoginScreen />,
  },
]);
