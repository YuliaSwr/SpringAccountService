package app.model;

import app.exception.UserException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {

    private String name;

    private String lastname;

    private String period;

    private String salary;

    public PaymentResponse(String name, String lastname, String period, long salary) {
        this.name = name;
        this.lastname = lastname;
        this.period = transformPeriod(period);
        this.salary = transformSalary(salary);
    }

    private String transformSalary(long salary) {
        return salary / 100 + " dollar(s) " + salary % 100 + " cent(s)";
    }

    public String transformPeriod(String period) {
        String month = period.substring(0,2);
        switch (month) {
            case "01":
                period = period.replaceFirst(month, "January");
                break;
            case "02":
                period = period.replaceFirst(month, "February");
                break;
            case "03":
                period = period.replaceFirst(month, "March");
                break;
            case "04":
                period = period.replaceFirst(month, "April");
                break;
            case "05":
                period = period.replaceFirst(month, "May");
                break;
            case "06":
                period = period.replaceFirst(month, "June");
                break;
            case "07":
                period = period.replaceFirst(month, "July");
                break;
            case "08":
                period = period.replaceFirst(month, "August");
                break;
            case "09":
                period = period.replaceFirst(month, "September");
                break;
            case "10":
                period = period.replaceFirst(month, "October");
                break;
            case "11":
                period = period.replaceFirst(month, "November");
                break;
            case "12":
                period = period.replaceFirst(month, "December");
                break;
            default:
                throw new UserException("Problems with period!");
        }
        return period;
    }
}
