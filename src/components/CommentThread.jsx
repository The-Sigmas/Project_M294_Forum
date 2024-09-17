import { useState, useEffect } from 'react';

export default function CommentThread({ threadId }) {
  const [threadData, setThreadData] = useState(null);
  const [newComment, setNewComment] = useState('');
  const [responseMessage, setResponseMessage] = useState('');

  useEffect(() => {
    fetch(`http://localhost:8080/threads/documents/${threadId}`)
      .then((res) => res.json())
      .then((data) => {
        setThreadData(data); // Save the entire thread data
      })
      .catch((error) => {
        console.error('Error fetching thread data:', error);
      });
  }, [threadId]);

  const handleAddComment = async (e) => {
    e.preventDefault();

    if (!threadData) return;

    const updatedComments = [newComment, ...(threadData.content.comments || [])];

    const updatedThreadData = {
      ...threadData,
      content: {
        ...threadData.content,
        comments: updatedComments,
      },
    };

    try {
      const response = await fetch(`http://localhost:8080/threads/documents/${threadId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedThreadData),
      });

      if (response.ok) {
        setThreadData(updatedThreadData);
        setNewComment('');
        setResponseMessage('Comment added successfully!');
      } else {
        setResponseMessage('Failed to add comment.');
      }
    } catch (error) {
      console.error('Error adding comment:', error);
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
        {threadData && threadData.content.comments.length > 0 ? (
          threadData.content.comments.map((comment, index) => (
            <div key={index} className="border p-3 rounded bg-gray-800">
              <p className="text-xl text-white">{comment}</p>
            </div>
          ))
        ) : (
          <p>No comments yet.</p>
        )}
      </div>
    </div>
  );
}
