import Footer from "./components/footer/index";
import Header from "./components/header/index";
import Sidebar from "./components/sidebar/index";
import Home from "./pages/home/index";


function AppRoutes() {
    return(
    <>
        <Header/>
        <div className = "d-flex">
            <Sidebar/>
            <div className = "flex-grow-1 p-4">
            <Home/>
            </div>
        </div>
        <Footer/>
    </>
    );
}

export default AppRoutes;