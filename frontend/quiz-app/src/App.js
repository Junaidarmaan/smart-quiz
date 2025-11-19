import './App.css';
import Auth from './Components/Authentication/Auth'
import { BrowserRouter } from 'react-router-dom';
import AppRouter from './Components/Router/AppRouter';
function App() {
  const authprops = {  
    login:false
  }
  return (
  <BrowserRouter>
    <AppRouter data={authprops}/>
  </BrowserRouter>
  );
}

export default App;
