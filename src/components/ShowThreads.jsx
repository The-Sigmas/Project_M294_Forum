import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../App.css';

export default function ShowThreads({ data: incoming }) {
  const [data, setData] = useState([]);

  useEffect(() => { if (incoming) setData(incoming); }, [incoming]);

  return (
    <>
      {data.length
        ? data.map((doc) => (
            <Link key={doc.id} to={`/threads/${doc.id}`}>
              <div className="m-4 bg-gray-800 rounded p-4 hover:bg-gray-700 transition-colors">
                <p className="text-xl text-white">{doc.content.title}</p>
                <div className="flex items-center text-sm text-gray-400 gap-4">
                  <span>Category: {doc.content.category || '—'}</span>
                  <span>Votes: {doc.content.votes ?? 0}</span>
                </div>
              </div>
            </Link>
          ))
        : <p>No threads found</p>}
    </>
  );
}
