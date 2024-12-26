import { User } from "./UserInterface";
export interface ReimbursementInterface {
  reimbursementId: string;
  description: string;
  amount: number;
  status: string;
  employee: User;
}
