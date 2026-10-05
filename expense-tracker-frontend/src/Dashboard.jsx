import { useEffect, useState } from "react";

function Dashboard() {
  const token = localStorage.getItem("token");

  const [summary, setSummary] = useState({
    totalIncome: 0,
    totalExpense: 0,
    balance: 0,
  });

  const [monthlySummary, setMonthlySummary] = useState({
    month: "",
    totalIncome: 0,
    totalExpense: 0,
    balance: 0,
  });

  const [transactions, setTransactions] = useState([]);
  const [categories, setCategories] = useState([]);

  const [showIncomeForm, setShowIncomeForm] = useState(false);
  const [showExpenseForm, setShowExpenseForm] = useState(false);

  const [editingTransaction, setEditingTransaction] = useState(null);

  const [incomeForm, setIncomeForm] = useState({
    title: "",
    amount: "",
    description: "",
    date: "",
  });

  const [expenseForm, setExpenseForm] = useState({
    title: "",
    amount: "",
    description: "",
    date: "",
    categoryId: "",
  });

  useEffect(() => {
    loadDashboard();
    loadMonthlySummary();
    loadTransactions();
    loadCategories();
  }, []);

  const loadDashboard = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/dashboard",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();

        setSummary({
          totalIncome: data.totalIncome || 0,
          totalExpense: data.totalExpense || 0,
          balance: data.balance || 0,
        });
      }
    } catch (error) {
      console.log("Error loading dashboard");
    }
  };

  const loadMonthlySummary = async () => {
    try {
      const today = new Date();

      const year = today.getFullYear();
      const month = today.getMonth() + 1;

      const response = await fetch(
        `http://localhost:8080/api/dashboard/monthly?year=${year}&month=${month}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();

        setMonthlySummary({
          month: data.month || "",
          totalIncome: data.totalIncome || 0,
          totalExpense: data.totalExpense || 0,
          balance: data.balance || 0,
        });
      }
    } catch (error) {
      console.log("Error loading monthly summary");
    }
  };

  const loadTransactions = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/dashboard/transactions",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        setTransactions(data);
      }
    } catch (error) {
      console.log("Error loading transactions");
    }
  };

  const loadCategories = async () => {
    try {
      const response = await fetch(
        "http://localhost:8080/api/categories",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        setCategories(data);
      }
    } catch (error) {
      console.log("Error loading categories");
    }
  };

  const handleIncomeChange = (e) => {
    setIncomeForm({
      ...incomeForm,
      [e.target.name]: e.target.value,
    });
  };

  const handleExpenseChange = (e) => {
    setExpenseForm({
      ...expenseForm,
      [e.target.name]: e.target.value,
    });
  };

  const addIncome = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(
        "http://localhost:8080/api/incomes",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(incomeForm),
        }
      );

      if (response.ok) {
        alert("Income added successfully");

        setIncomeForm({
          title: "",
          amount: "",
          description: "",
          date: "",
        });

        setShowIncomeForm(false);

        loadDashboard();
        loadMonthlySummary();
        loadTransactions();
      } else {
        alert("Could not add income");
      }
    } catch (error) {
      console.log(error);
      alert("Could not connect to the server");
    }
  };

  const addExpense = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(
        "http://localhost:8080/api/expenses",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            ...expenseForm,
            categoryId: Number(expenseForm.categoryId),
          }),
        }
      );

      if (response.ok) {
        alert("Expense added successfully");

        setExpenseForm({
          title: "",
          amount: "",
          description: "",
          date: "",
          categoryId: "",
        });

        setShowExpenseForm(false);

        loadDashboard();
        loadMonthlySummary();
        loadTransactions();
      } else {
        alert("Could not add expense");
      }
    } catch (error) {
      console.log(error);
      alert("Could not connect to the server");
    }
  };

  const startEdit = (transaction) => {
    const isIncome =
      transaction.type?.toUpperCase() === "INCOME";

    setEditingTransaction(transaction);

    if (isIncome) {
      setIncomeForm({
        title: transaction.title || "",
        amount: transaction.amount || "",
        description: transaction.description || "",
        date: transaction.date || "",
      });

      setShowIncomeForm(true);
      setShowExpenseForm(false);
    } else {
      const category = categories.find(
        (item) => item.name === transaction.categoryName
      );

      setExpenseForm({
        title: transaction.title || "",
        amount: transaction.amount || "",
        description: transaction.description || "",
        date: transaction.date || "",
        categoryId: category ? category.id : "",
      });

      setShowExpenseForm(true);
      setShowIncomeForm(false);
    }

    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };

  const cancelEdit = () => {
    setEditingTransaction(null);

    setIncomeForm({
      title: "",
      amount: "",
      description: "",
      date: "",
    });

    setExpenseForm({
      title: "",
      amount: "",
      description: "",
      date: "",
      categoryId: "",
    });

    setShowIncomeForm(false);
    setShowExpenseForm(false);
  };

  const updateIncome = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(
        `http://localhost:8080/api/incomes/${editingTransaction.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(incomeForm),
        }
      );

      if (response.ok) {
        alert("Income updated successfully");

        cancelEdit();
        loadDashboard();
        loadMonthlySummary();
        loadTransactions();
      } else {
        alert("Could not update income");
      }
    } catch (error) {
      console.log(error);
      alert("Could not connect to the server");
    }
  };

  const updateExpense = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch(
        `http://localhost:8080/api/expenses/${editingTransaction.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            ...expenseForm,
            categoryId: Number(expenseForm.categoryId),
          }),
        }
      );

      if (response.ok) {
        alert("Expense updated successfully");

        cancelEdit();
        loadDashboard();
        loadMonthlySummary();
        loadTransactions();
      } else {
        alert("Could not update expense");
      }
    } catch (error) {
      console.log(error);
      alert("Could not connect to the server");
    }
  };

  const deleteTransaction = async (transaction) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this transaction?"
    );

    if (!confirmed) {
      return;
    }

    try {
      let url;

      if (transaction.type?.toUpperCase() === "INCOME") {
        url = `http://localhost:8080/api/incomes/${transaction.id}`;
      } else {
        url = `http://localhost:8080/api/expenses/${transaction.id}`;
      }

      const response = await fetch(url, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        alert("Transaction deleted");

        loadDashboard();
        loadMonthlySummary();
        loadTransactions();
      } else {
        alert("Could not delete transaction");
      }
    } catch (error) {
      console.log(error);
      alert("Could not connect to the server");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    window.location.reload();
  };

  const maxAmount = Math.max(
    Number(summary.totalIncome),
    Number(summary.totalExpense),
    1
  );

  const incomeHeight =
    (Number(summary.totalIncome) / maxAmount) * 200;

  const expenseHeight =
    (Number(summary.totalExpense) / maxAmount) * 200;

  return (
    <div className="dashboard">

      {/* Header */}

      <div className="dashboard-header">

        <div>
          <h1>Expense Tracker</h1>
          <p>Manage your money easily</p>
        </div>

        <button
          className="logout-button"
          onClick={handleLogout}
        >
          Logout
        </button>

      </div>


      {/* Overall Summary */}

      <div className="summary-container">

        <div className="summary-card income-card">
          <h3>Total Income</h3>

          <h2>
            ₹{Number(summary.totalIncome).toLocaleString("en-IN")}
          </h2>
        </div>

        <div className="summary-card expense-card">
          <h3>Total Expense</h3>

          <h2>
            ₹{Number(summary.totalExpense).toLocaleString("en-IN")}
          </h2>
        </div>

        <div className="summary-card balance-card">
          <h3>Balance</h3>

          <h2>
            ₹{Number(summary.balance).toLocaleString("en-IN")}
          </h2>
        </div>

      </div>


      {/* Monthly Summary */}

      <div className="monthly-box">

        <h2>
          This Month
          {monthlySummary.month
            ? ` - ${monthlySummary.month}`
            : ""}
        </h2>

        <div className="monthly-items">

          <div>
            <span>Income</span>
            <strong className="income-text">
              ₹
              {Number(
                monthlySummary.totalIncome
              ).toLocaleString("en-IN")}
            </strong>
          </div>

          <div>
            <span>Expense</span>
            <strong className="expense-text">
              ₹
              {Number(
                monthlySummary.totalExpense
              ).toLocaleString("en-IN")}
            </strong>
          </div>

          <div>
            <span>Balance</span>
            <strong className="balance-month-text">
              ₹
              {Number(
                monthlySummary.balance
              ).toLocaleString("en-IN")}
            </strong>
          </div>

        </div>

      </div>


      {/* Buttons */}

      <div className="action-buttons">

        <button
          className="add-income-button"
          onClick={() => {
            setEditingTransaction(null);
            setShowIncomeForm(!showIncomeForm);
            setShowExpenseForm(false);
          }}
        >
          + Add Income
        </button>

        <button
          className="add-expense-button"
          onClick={() => {
            setEditingTransaction(null);
            setShowExpenseForm(!showExpenseForm);
            setShowIncomeForm(false);
          }}
        >
          + Add Expense
        </button>

      </div>


      {/* Income Form */}

      {showIncomeForm && (

        <div className="form-box">

          <h2>
            {editingTransaction
              ? "Edit Income"
              : "Add Income"}
          </h2>

          <form
            onSubmit={
              editingTransaction
                ? updateIncome
                : addIncome
            }
          >

            <input
              type="text"
              name="title"
              placeholder="Title"
              value={incomeForm.title}
              onChange={handleIncomeChange}
              required
            />

            <input
              type="number"
              name="amount"
              placeholder="Amount"
              value={incomeForm.amount}
              onChange={handleIncomeChange}
              required
            />

            <input
              type="text"
              name="description"
              placeholder="Description"
              value={incomeForm.description}
              onChange={handleIncomeChange}
            />

            <input
              type="date"
              name="date"
              value={incomeForm.date}
              onChange={handleIncomeChange}
              required
            />

            <button type="submit">
              {editingTransaction
                ? "Update Income"
                : "Save Income"}
            </button>

            {editingTransaction && (
              <button
                type="button"
                className="cancel-button"
                onClick={cancelEdit}
              >
                Cancel
              </button>
            )}

          </form>

        </div>

      )}


      {/* Expense Form */}

      {showExpenseForm && (

        <div className="form-box">

          <h2>
            {editingTransaction
              ? "Edit Expense"
              : "Add Expense"}
          </h2>

          <form
            onSubmit={
              editingTransaction
                ? updateExpense
                : addExpense
            }
          >

            <input
              type="text"
              name="title"
              placeholder="Title"
              value={expenseForm.title}
              onChange={handleExpenseChange}
              required
            />

            <input
              type="number"
              name="amount"
              placeholder="Amount"
              value={expenseForm.amount}
              onChange={handleExpenseChange}
              required
            />

            <input
              type="text"
              name="description"
              placeholder="Description"
              value={expenseForm.description}
              onChange={handleExpenseChange}
            />

            <input
              type="date"
              name="date"
              value={expenseForm.date}
              onChange={handleExpenseChange}
              required
            />

            <select
              name="categoryId"
              value={expenseForm.categoryId}
              onChange={handleExpenseChange}
              required
            >

              <option value="">
                Select Category
              </option>

              {categories.map((category) => (
                <option
                  key={category.id}
                  value={category.id}
                >
                  {category.name}
                </option>
              ))}

            </select>

            <button type="submit">
              {editingTransaction
                ? "Update Expense"
                : "Save Expense"}
            </button>

            {editingTransaction && (
              <button
                type="button"
                className="cancel-button"
                onClick={cancelEdit}
              >
                Cancel
              </button>
            )}

          </form>

        </div>

      )}


      {/* Main Content */}

      <div className="dashboard-content">

        {/* Transactions */}

        <div className="transactions-box">

          <h2>Recent Transactions</h2>

          {transactions.length === 0 ? (

            <p>No transactions found.</p>

          ) : (

            transactions.map((transaction) => {

              const isIncome =
                transaction.type?.toUpperCase() === "INCOME";

              return (
                <div
                  className="transaction"
                  key={transaction.id}
                >

                  <div>

                    <strong>
                      {transaction.title}
                    </strong>

                    <small>
                      {transaction.date}

                      {transaction.categoryName
                        ? ` • ${transaction.categoryName}`
                        : ""}
                    </small>

                  </div>


                  <div className="transaction-right">

                    <span
                      className={
                        isIncome
                          ? "income-text"
                          : "expense-text"
                      }
                    >
                      {isIncome ? "+" : "-"} ₹
                      {Number(
                        transaction.amount
                      ).toLocaleString("en-IN")}
                    </span>

                    <button
                      className="edit-button"
                      onClick={() =>
                        startEdit(transaction)
                      }
                    >
                      Edit
                    </button>

                    <button
                      className="delete-button"
                      onClick={() =>
                        deleteTransaction(transaction)
                      }
                    >
                      Delete
                    </button>

                  </div>

                </div>
              );
            })

          )}

        </div>


        {/* Graph */}

        <div className="chart-box">

          <h2>Income vs Expense</h2>

          <div className="chart">

            <div className="bar-container">

              <div
                className="bar income-bar"
                style={{
                  height: `${incomeHeight}px`,
                }}
              ></div>

              <span>Income</span>

            </div>


            <div className="bar-container">

              <div
                className="bar expense-bar"
                style={{
                  height: `${expenseHeight}px`,
                }}
              ></div>

              <span>Expense</span>

            </div>

          </div>

        </div>

      </div>

    </div>
  );
}

export default Dashboard;