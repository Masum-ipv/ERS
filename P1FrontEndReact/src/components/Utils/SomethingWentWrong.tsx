interface Props {
  message: string;
}

export default function SomethingWentWrong({ message }: Props) {
  return (
    <div className="text-center alert-danger mt-5">
      Something went wrong {message}
    </div>
  );
}
