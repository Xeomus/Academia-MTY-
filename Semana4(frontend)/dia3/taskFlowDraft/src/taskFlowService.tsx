import { useEffect, useState } from "react";

interface TaskFlowInfo {
  version: string;
  app: string;
}

function TaskFlowService() {
  const [info, setInfo] = useState<TaskFlowInfo | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch("https://d3ujwk09smrk9z.cloudfront.net/info")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        return response.json();
      })
      .then((data: TaskFlowInfo) => {
        setInfo(data);
      })
      .catch((error: Error) => {
        setError(error.message);
      });
  }, []);

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!info) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h1>Task Flow Service</h1>
      <p>Version: {info.version}</p>
      <p>App: {info.app}</p>
    </div>
  );
}

export default TaskFlowService;
