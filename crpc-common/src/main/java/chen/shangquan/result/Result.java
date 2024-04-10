package chen.shangquan.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Boolean success;
    private Integer code;
    private String message;
    private Object data;
    private Long total;
    private List<Object> list;

    public static Result ok(){
        return new Result(true,200,null,null,null,null);
    }
    public static Result ok(String message){
        return new Result(true,200,message,null,null,null);
    }
    public static Result ok(Object data){
        return new Result(true,200,null,data,null,null);
    }
    public static Result ok(Object data,String message){
        return new Result(true,200,message,data,null,null);
    }
    public static Result returnList(List<Object> list){
        return new Result(true,200,null,null,null,list);
    }
    public static Result fail(String message){
        return new Result(true,400,message,null,null,null);
    }
    public static Result authFail(String message){
        return new Result(true,401,message,null,null,null);
    }
    public static Result fail(Object data,String message){
        return new Result(true,400,message,data,null,null);
    }
    public static Result noAuth(String message) {
        return new Result(true,401,message,null,null,null);
    }
}