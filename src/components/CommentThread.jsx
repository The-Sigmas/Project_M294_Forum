import { useState, useEffect } from 'react';

export default function CommentThread({ threadId }) {
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');
  const [responseMessage, setResponseMessage] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/threads/documents')
      .then((res) => res.json())
      .then((threads) => {
        const thread = threads.find((t) => t.id === threadId);
        if (thread) {
          setComments(thread.content.comments || []);
        }
      })
      .catch((error) => console.error(error));
  }, [threadId]);

  const handleAddComment = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('http://localhost:8080/threads/documents');
      const threads = await response.json();
      const threadToUpdate = threads.find((t) => t.id === threadId);

      if (!threadToUpdate) {
        setResponseMessage('Thread not found.');
        return;
      }

      const updatedComments = [newComment, ...(threadToUpdate.content.comments || [])];
      threadToUpdate.content.comments = updatedComments;

      const updateResponse = await fetch(`http://localhost:8080/threads/documents/${threadToUpdate.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: threadToUpdate.content }),
      });

      if (updateResponse.ok) {
        setComments(updatedComments);
        setNewComment('');
        setResponseMessage('Comment added successfully!');
      } else {
        setResponseMessage('Failed to add comment.');
      }
    } catch (error) {
      console.error(error);
      setResponseMessage('An error occurred.');
    }
  };

  return (
    <div className="space-y-6">
      <h3 className="text-xl font-semibold">Comments</h3>
      <form onSubmit={handleAddComment} className="space-y-4">
        <textarea
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          placeholder="Write your comment..."
          required
          className="block w-full p-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-red-500"
        />
        <button
          type="submit"
          className="px-4 py-2 bg-red-600 text-white rounded-lg shadow hover:bg-red-700"
        >
          Add Comment
        </button>
      </form>

      {responseMessage && <p>{responseMessage}</p>}

      <div className="space-y-4">
        {comments.length > 0 ? (
          comments.map((comment, index) => (
            <div key={index} className="border p-3 rounded-lg bg-gray-100">
              <p>{comment}</p>
            </div>
          ))
        ) : (
          <p>No comments yet.</p>
        )}
      </div>
    </div>
  );
}

