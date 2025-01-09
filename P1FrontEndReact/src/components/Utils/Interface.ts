export interface ReimbursementInterface {
  reimbursementId: string;
  description: string;
  amount: number;
  status: string;
  employee: User;
}

export interface User {
  employeeId: string;
  name: string;
  email: string;
  role: string;
}
