import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import CommentThread from './CommentThread.jsx';
import '../App.css';

export default function ViewThread({ id }) {
  const [thread, setThread] = useState(null);
  const [msg, setMsg] = useState('');

  /* load thread */
  useEffect(() => {
    if (!id) return;
    fetch(`http://localhost:8080/api/threads/${id}`)
      .then((r) => r.json())
      .then((doc) => setThread(doc.content))
      .catch(console.error);
  }, [id]);

  /* upvote */
  const upvote = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/threads/${id}/upvote`, { method: 'PUT' });
      if (res.ok) {
        const updated = await res.json();
        setThread(updated.content);
      }
    } catch (err) { console.error(err); }
  };

  /* delete */
  const handleDelete = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/threads/${id}`, { method: 'DELETE' });
      setMsg(res.ok ? 'Thread deleted' : 'Delete failed');
    } catch (err) { console.error(err); setMsg('Error'); }
  };

  return (
    <>
      {thread ? (
        <>
          <div className="bg-gray-800 p-6 m-2 rounded-lg space-y-2">
            <h1 className="text-2xl text-white font-bold">{thread.title}</h1>
            <p className="text-gray-300">{thread.content}</p>
            <p className="text-sm text-gray-400">Category: {thread.category || '—'}</p>

            <div className="flex items-center gap-4 mt-2">
              <span className="text-gray-200">Votes: {thread.votes ?? 0}</span>
              <button
                onClick={upvote}
                className="px-3 py-1 bg-red-600 text-white rounded hover:bg-red-700"
              >
                Upvote
              </button>
            </div>
          </div>

          <hr />

          <Link
            to="/"
            onClick={handleDelete}
            className="px-4 py-2 bg-red-600 text-white rounded-lg shadow hover:bg-red-700"
          >
            Delete Thread
          </Link>

          {msg && <p>{msg}</p>}
          <hr />

          <CommentThread threadId={id} />
        </>
      ) : (
        <p>Loading thread…</p>
      )}
    </>
  );
}
