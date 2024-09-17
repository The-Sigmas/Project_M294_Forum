import { useState, useEffect } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import * as React from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

// Components
import NavBar from "./components/NavBar.jsx"
import About from "./components/About.jsx"
import CreateThread from "./components/CreateThread.jsx"

function App() {
  const [count, setCount] = useState(0)
  const [data, setData] = useState([])

  useEffect(() => {
    const fetchAllData = async () => {
      try {
        const response = await fetch('http://localhost:8080/threads/documents', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
        })

        if (response.ok) {
          const raw = await response.json()
          setData(raw)
        } else {
          console.error('Failed to fetch Raw Data, Status: ', response.status)
        }
      } catch (error) {
        console.error('Following error occured: ', error)
      }
    }

    fetchAllData()
  }, [])

  return (<BrowserRouter>
            <div>
              <Routes>
                <Route path="/" element={<NavBar />}>
                  <Route index element={<h1 className="bg-gradient-to-r from-red-500 via-red-700 to-red-900 bg-clip-text text-transparent">Welcome to The Forums</h1>} />
                    <Route path="/about" element={<About />} />
                    <Route path="/createthread" element={<CreateThread />}/>
                    {data.length > 0 ? (
                      data.map((doc) => (
                        <Route path={`/threads/${ doc._id }`} element={<ViewThread id={`${doc._id}`}/>} />
                      ))
                    ) : (
                        <Route path="/error" element={<About />}>
                      )}
                </Route>
              </Routes>
            </div>
            <div>
              {data.length > 0 ? (
                data.map((doc) => (
                  <Link to={`/threads/${doc._id}`}>
                    <div>
                      <p>doc.title</p>
                    </div>
                  </Link>
                ))
              ) : (
                <p>No data found</p>
              )}
            </div>
          </BrowserRouter>)
}

export default App
