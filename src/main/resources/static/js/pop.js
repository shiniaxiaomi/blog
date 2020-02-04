/**
 * Created by GaoWei on 2017/4/13.
 */
/*
 本弹窗插件主要提供四种形式的操作交互与提示：
 1.alert:操作完成后的提示弹窗，调用方法：pop.alert(msg,type,fun)，msg为提示信息，type可取值'success'/'fail'/'warning'/'smile'，fun为倒计时3秒后执行的函数体
 2.confirm:确定或取消交互弹窗，调用方法：pop.confirm(msg,fun)，msg为提示信息，fun为点击弹窗中的 确定 按钮后执行的函数体
 3.open:操作进行时的警示弹窗，调用方法：pop.open(msg,type,fun)，msg为提示信息，type可取值'success'/'fail'/'warning'/'smile'，fun为点击弹窗中的 确定 按钮后执行的函数体
 4.prompt:响应事件后的简短消息提示，比如表单验证blur事件后的错误提示，调用方法：pop.prompt(msg,target,time), msg为提示信息，target为要插入到的目标元素，留空则插入body并居中显示，time为提示窗口显示的时间，留空则默认为1.5s
 另外：以上参数中msg为必填，type/fun为选填，但是如果要填就必须按照 msg>type>fun 这样的顺序
 */
var pop = {
    dialog: '<div id="new_dialog">' +
    '<style>' +
    '*{margin:0; padding: 0;}' +
    '#new_dialog{position:fixed; top: 0; left: 0; width: 100%; height: 100%; font-size: 14px; z-index: 123456789; }' +
    '.dialog_overlay{background: rgba(0,0,0,0.3); width: 100%; height: 100%; position: absolute; z-index: 123456788;}' +
    '.dialog_content{transition:all 0.3s; background: #fff; position: absolute; z-index: 123456789; top: 50%; left: 50%; transform: scale(1); border-radius: 3px; display: block;}' +
    '</style>' +
    '<div class="dialog_overlay"></div>' +
    '</div>',
    success: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAYAAAA7MK6iAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NzFFRUY3MEIyMDQ0MTFFN0IyNDI4RUIwNzFFMkI2RTciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NzFFRUY3MEMyMDQ0MTFFN0IyNDI4RUIwNzFFMkI2RTciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3MUVFRjcwOTIwNDQxMUU3QjI0MjhFQjA3MUUyQjZFNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3MUVFRjcwQTIwNDQxMUU3QjI0MjhFQjA3MUUyQjZFNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pnt2okMAAAQeSURBVHjavJfdT5NXHMe/z0MLVAoqKHaiQcWMN8E5jVmMzv0Bu9HIEMU736NLXPRS44V3xqg36MQLL3azLdl27WRg3KKYmNCWF2EENbpZmLTSlkKx9Hi+xz5NKe3T8jK/yQnN8/L7cM75/X7n+2hCCGTSv+GQuP/Wg8dj/8EVHMXguB8jUxPqXmmuDRsLilBvL8H2pSuxe5kDq/OWaJliambgnnGf+Gn4GX4deY7ugBfZaFNhMfaUrsM3q9ajtmC5Nicwr9x89VS0/tOHLv8oKguWYXexA9uLSlFnX471tkIUW/LgjYTxbCIAd9CHx/4R3Pd60D/+VsX4rKgER8qqcXxNlaZlA/ZMTYjLL9y4+tyFrXLpGldtQIP878vz7RlnK9/FD68H8ePwEJ7IbaHOrKvHufI6OHJtM/kEG+O13MujvQ+EdrdVnOj7U7gDXjEfDYX86n3G4WBMxk5kxX9E5fhuoFPY2+6IS0NdIioWJr7POIxHOGNHU4FbXvapB/jwYorxjJmTMQPcHfSKLY9+UcsTFYsrxjOWnQyyyNS5zyyZHE3HyTU10LBw9cnM7owlF+OdK69XicoKIYvS2RxYp8zeTbJUFiqW1l7nPexx/o5HYyPqGsuP8SmyyNTZkd5Fo6pkFqquwCgaXG2qlj3hEE73P4zfa/5ko+oHbERk6myDbA7Z1GkmaJO7HQOxBmLPseLbtbXx+7KOFYciU2fvZUdKpSf+N+gPjWWEuoJeNPd0xLtWgYTeqNmJQ3KWiTI4ZOps+HVJe8te1uHz4Ouuu9jnuocBE7iCdnegN+CLz/RW7S4cdFTMetbgkKnzlOHmJ8r3LoxT/X9hWO5TjwzY6P4j5cwJbXK1xw+QQosVrbVfoimWSMkyOGSqcmLDT9SSHIsqLf6lnLIMCO+NLWU8kZxtsnQ+zLTIkovbEtpokqSJHJ3nKU+ZROXrORJcjWtVO2CLwV0S3tzdjiF5GrFO98tE+ju2Cpzp9zW70FBqXhkGh0wLD3EebSus+bMePLz6U0yLKM48fYjJ6LRqAHvlnociEQzGoNzTG9U7TWdqiByKTJ3OgUWfTsfKqnA9aeaDiTOViXQgRSKlay4UmTrtCg9xMx0pq8SVyi9mXefypkukVDI4ZOr0SJ7wBF5MBk1fOi5nfqFi6wzo/jlAGZ8OhdaITAuN2ebCEvGzbN5npVMw08UNW1TiFVvz1CrMRYzPBnO+4vMPZtA4FmXGzttxZBLjbuv8bfaxSDe4Y6kDLa96IbC4YjzGpQej+Ys7z2Tr88Dn+V8cSFrrwyFbpQhEphbNeRiey9TsJY2v5OiYnI7Me08Nu8OZJkM50n5JRGTHapGmfkp2rGx9NUuG2UtfzY43J0Of6hPGKQ8ER54tqy8Jq67P/xPmY3y0vRdgADJlJv30BUNTAAAAAElFTkSuQmCC',
    fail: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAIAAAD8GO2jAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RjE4QjY3QzMyMDU0MTFFNzgxNzNFRUI2MEExOUUzQzIiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RjE4QjY3QzQyMDU0MTFFNzgxNzNFRUI2MEExOUUzQzIiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpGMThCNjdDMTIwNTQxMUU3ODE3M0VFQjYwQTE5RTNDMiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpGMThCNjdDMjIwNTQxMUU3ODE3M0VFQjYwQTE5RTNDMiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Ptl5690AAANMSURBVHjatFZbSFNxGN+5bbopdhxhFAQ9SEMEK6KC6GJYZpYtbyH2oAWhFUSYUdBD9FL5EkQU9FJkEOmDQqWBBV2QUKgYmfhUaTDntrM55+a2c+t35vCyzbOL7ns4/Dnf9/2+//+7E7Isa1YmWRRClu/BoS/CxB/RzYkch5+U0UixRnrzFt3ufdqSHQRFqyAQKxmQ3K7Zl0/9HwdkPqArKmFMxQAlWWOYxcEYPzYSHLUQTJb+4OGchmaSzU/WgMzzvlfPfL1d1PoCQ90Z3a69ZE5u/EvMeoPDg77uF6JjymCuN5xuIhgmxgnLSXS7HG3nbY1VvnevZUmSkyFJgjBUoAj1KOYyA/zf3/amU87LZwWnQ06RoAJFqAMkvgEYB9t1+7oUDMhpERShDpCl74jEAH7nblwiBCG/4xGh1WnSJTkUdF27INO08c7DSDzm7Xg7n8CJaXgmrq8ABcBFF4kuzlZTpkR1jUiJeU0ZYHEm8QjkOzJSf6Qy6r3Iv7n3b9V9AgGIRf0EFAABizOJWkU1Id81BBElR7Ls9IMOf3/vSuhgQQBiMdVFAFApUlHQBL4NWc0HRO9M/Mf29VhP7Mc3JZbidu8MYAFOtW8zkbpsfcXJuHdkCk3UOtbz+D6+OC+9O37mtV7RV5jjdwitjv/5Q/Z6aHQxpqhExcvzEIBbOCdEj1zOVMyPWmi0raxwC0vSxvwhIbrSdFljwM3R6MBkIgNRNpJBD+eIUQHXZI4kgWRoGtMD/T2h8ILfo+KhQqJtktqwiYanxEQGYqOajA3RbqUKNtKYfJhNKaHH5lVcEiatTGFRxgptxmOrPhQYHkQk+Mm6cv+H/lgh/8AbFYgFGxCLZc12Pbc312LY0dgJMLXRsLJLy6PakTAxnnexXV9+XC13SZTqeEz+SP6+Hv2xagUwE+06NDZiM5eKHvfiyFzDgaPMzlCI/zeekZEZp54jzY9h8m/elTzu6Xu3MFfTLV0JCGp70erWFrvzaouzrUVtL0p78Zr7NDDVUMm1twpc9M1Wtzr6fcGvn33dnaLDoTfX5zae05DkqpdfgpCmOcnlDP2yBMdGCFprOFplqG0k89jUtmu19Z0Nr++FW3U792iLtxO02vr+X4ABAE2Wg9Agn3pmAAAAAElFTkSuQmCC',
    warning: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAIAAAD8GO2jAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QjA2MTczRTIyMDU4MTFFNzkzRDVFQzVCN0VDNkM4MzciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QjA2MTczRTMyMDU4MTFFNzkzRDVFQzVCN0VDNkM4MzciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpCMDYxNzNFMDIwNTgxMUU3OTNENUVDNUI3RUM2QzgzNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpCMDYxNzNFMTIwNTgxMUU3OTNENUVDNUI3RUM2QzgzNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PhBDvcAAAAMDSURBVHjatJZLaxNRFMczd6aTaZLJ5GFqGrDUgKn1sVC7KD5AorsuuijiJ3DRTdGF4gdQ8LHQj+BSFwUVRaHgpq2oKIgWa4KitLQ1zcM8nGSSzMz1XxNSY01mEiaHu5rc+/vPnJx7/oehlNpah6bTj5vldxvKak7NKlpWUfHQI3Aegd0jcccGhcMDdpYwbQhMKwHgZj8V5leKFY2OBuwRHy8JrEcgf37Sc4oWz1SWk2WeZU4NOaYOiJA0K6BqdPZz4Wm8sMvBTY6IYyHByZP/HpYr+tt15VGskCqqExFxar/IsYyBAF7t1stMUlbPH3JHh51Mu6+vBwAvvssPlvIBJ3fluE9q/pQmgZVc9cZC2m0nl0/4/f2srZNIl7Tbi+l8Wb960j8k9f2tX49sSZ1+snFzIVVWddpV4CCOAwJU4yFp5B2ZwbtfHPfxLGPrKnAQxwEBCsDaw7oA/lXkHZnpmt7QAAQoALcFUJGoGfyrhnm/Pp/Car8HEKAABLYugHpHRaJmDF/wQ6KMZbgNKACB3RLAXcVtQr2bqcjpMS+W4TagAAQWcIJOgLuK22QmxaeHHVhmdgIILOAEfQadoNVd/bd/lLRaZg0DQGABJ+hi6DMmi2TmeWLmWcLkZmAB5/BGkmD20uq0g5IFFnCCDlzrkZYHsID3BN34XIKAe6C/90JgU1Z3O1kIsDlzhdFpJGVtwMkROB+8qRcCP+Rq0MUR+CqcD95kLf1XWY+lqyN+nsC10QLhfNYKzH2TPXbmSFAgmAng2vBVSq2sn7mv8tmwC01pq0wxE8C14auGJwdFDstw25dMBd4Z3evctsz7S7kLj9chQ62Iqqqv5apNlomJAzMBXLuiWZApDC8hN9dkmXiEiQPfdfdVpmsNpD6/485aNrZkitqd12lKmWvRgPWD1+Jq8d77bEjsuzTu8/azlo2Oxar+Zk15GMtnivrEPte5g+6dc3DHwy8QPxUdvy4nK/E0hl/bmbBrMiK6W/R8prvxXRJI2MsfDQqjAZ5rO77/FmAAsY1XspK0u6YAAAAASUVORK5CYII=',
    smile: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAIAAAD8GO2jAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NkQ4RkQyQTgyMDVBMTFFNzhBQzdFMzVFQkFFRkI0NDQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NkQ4RkQyQTkyMDVBMTFFNzhBQzdFMzVFQkFFRkI0NDQiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo2RDhGRDJBNjIwNUExMUU3OEFDN0UzNUVCQUVGQjQ0NCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo2RDhGRDJBNzIwNUExMUU3OEFDN0UzNUVCQUVGQjQ0NCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PkjUxU8AAAR4SURBVHjarFZ7bFNlFL/vLlvL2rllW4FO0NAJIQoIIotOlKXOGUwQowEfGCGagNH/lijqJsHFP3zEaZRsIpgpw+ck28JgCIplMZmZEke6Dscqe/Rdem/btffxfZ6WTmh715Wxk9vm3u+ce853vnPu7/xIjDGRg6CIDwlOuAisULry+FVQnMuLTHa1dOlsbKhLtHUrwcvwiCmWJEgCiXBP6srzzLVcZR239AGCpGbyQM6UgXjxVLi3QfIMcab1mmUPs7dWUQuMVH4xQZIo7IVUpH/PiUM9osNK6U3aB/dqlm9Wj4AzBE0FA21bXY3F/LFXFd6Js4oS9grd9e59pYHPLYrgzjRIDyD7/vE2r/V9co/sseOcRfaP+g5s9L6/QnL+nS2A7BvxvnvblS+3oCiPb1CQOBVsf8b9ziLJdUE9ADj1fbw2cPgxrMh4boJQ8Mg23wcrlZDn/7Vr1Rc6dhMIFT5xiKBoYm5CkrotLWSejv/uhfQii6NWV0ORND6Ab1pkz7Dr7ZKorTslg9DJt/JWPs4Y7yJuWuji2/PXPBvubSQwSmYgjvXD9uWAA8+TQA2gcWMXTyUzEG2drHEVrTcR8ySAIlxFlWjrit/DL2br0pgtmXay6wLfvl348UUlOKaCTiG3cOzl4FdbpbH+TC1ntkQTARgsi4p3mDXdm2kU6nlNuvRL4lw53ebmNG3kXHN0oC0eSXAZXjqbpmVNG3CoHoU9FAo544noytTwKJzsNDGigjHTWiRNqZxSwiFAFhNH4PizMdOowLI/3PM6qdEWVNdnavOrXlH4cSw4Cx56U7UMBMUhYZKZRloVTGUXr9PvPDljOxoq9NuOzljoBEoQJE1N5zJJzKugiJdEEjinaG0pJiiFn5jnAAmH8QAEzbKly6XR31I6xPqhaO/J3Z043Bs+05QyCketdOFiKv+WeAG4ykfS3JFsAf/tDtF+PBfvMVtnsH07SXEpi/bj4DZZDGniL3eDXvYOX/+5R/o+BcyCf4yULKgw1X8IzEKnm1Kggp+ERXHk12vzIPBFHYyLjJcPevYb/Z/dLzr6Ml0D9AZaazz7SsPWj9JUfMceeOvqfXLoyxMD/pZNhue7WNP61FpNhk7sjQ3+QOrKOONqxrgapgUYS+N/IH6cM9dqLU3Qr6kAM+g/UK1/+ntuaXUKqxB+2iOOnDHsOk1pS9JOWfHYpcu/S+B34k8SK4xxFWu8k1m0jildkd790aC/tYYtWbbgybZ02oLlWOBgLUUzhc91kgw3p95UAPugAIadvfD9q9AW0HneuwNOVpWAzDKPIwGY58AZgDlcv06lIVTRrp+RIgdaNkqOvty3DlUJtG5CwgRAC120ZDbiJUX5jt0w4658/ZTkHJxlAnuHg9/sAGNoQlWyMyN1hE2FTrwhOaxMSSVX+Si75D4aqKOuHMoGO41TR4c1ZuuWneeZhXdraxrZig03xk2TYSbPXyW/QKeA9KagaYlZY66Dz5VduCYblcmRvhOKpIRcCfqO4hCmKyPpnDrtPwEGAG6n6pq0DO4bAAAAAElFTkSuQmCC',
    appendDialog: function () { // 公共父元素及样式插入函数
        $('body').append(pop.dialog);
    },
    clickDialogOverlay: function (target) { // 点击阴影背景移除弹层
        $('.dialog_overlay').click(function () {
            pop.targetGoHide(target);
        });
    },
    clickCloseBtn: function (target) { // 点击关闭按钮
        $('.close_btn').click(function () {
            pop.targetGoHide(target);
        });
    },
    clickSureBtn: function (target, fun) { // 点击确定按钮
        $('.sure_btn').click(function () {
            pop.targetGoHide(target);
            fun && setTimeout(fun, 300);
        });
    },
    clickCancelBtn: function (target) { // 点击取消按钮
        $('.cancel_btn').click(function () {
            pop.targetGoHide(target);
        });
    },
    targetGoHide: function (target) { // dialog执行隐藏函数
        if (target == 'alert') {
            pop.alertDialogAnimate('hide');
        }
        else if (target == 'confirm') {
            pop.confirmDialogAnimate('hide');
        }
        else if (target == 'open') {
            pop.openDialogAnimate('hide');
        }
    },
    removeDialog: function () { // 移除dialog
        $('#new_dialog').remove();
    },
    alertDialogAnimate: function (action) { // alert弹窗动画
        if (action == 'show') {
            setTimeout(function () {
                $('.dialog_content').css('transform', 'scale(1)');
            }, 100)
        }
        else if (action == 'hide') {
            $('.dialog_content').css('transform', 'scale(0)');
            setTimeout(function () {
                pop.removeDialog();
            }, 300)
        }
    },
    confirmDialogAnimate: function (action) { // confirm弹窗动画
        if (action == 'show') {
            setTimeout(function () {
                $('.dialog_content').css('transform', 'rotateY(0deg)');
            }, 100)
        }
        else if (action == 'hide') {
            $('.dialog_content').css('transform', 'rotateY(90deg)');
            setTimeout(function () {
                pop.removeDialog();
            }, 300)
        }
    },
    openDialogAnimate: function (action) { // open弹窗动画
        if (action == 'show') {
            setTimeout(function () {
                $('.dialog_content').css('transform', 'rotateX(0deg)');
            }, 100)
        }
        else if (action == 'hide') {
            $('.dialog_content').css('transform', 'rotateX(90deg)');
            setTimeout(function () {
                pop.removeDialog();
            }, 300)
        }
    },
    isHasIcon: function (type) { // 图标有无检测
        if (type == '' || type == undefined) {
            return '';
        }
        else {
            return 'padding-left:40px; background: url(' + pop.iconCheck(type) + ') no-repeat left center;'
        }
    },
    iconCheck: function (type) { // 图标类型检测
        if (type == 'success') {
            return pop.success;
        }
        else if (type == 'fail') {
            return pop.fail;
        }
        else if (type == 'warning') {
            return pop.warning;
        }
        else if (type == 'smile') {
            return pop.smile;
        }
    },
    alert: function (msg, type, fun, s) { // alert弹窗
        pop.appendDialog();
        if (typeof(type) == 'undefined') { // 如果只传来msg
            s = 3;
        }
        if (typeof(type) == 'string') { // 如果传来msg,type
            if (typeof(fun) == 'number') {
                s = fun;
            } else {
                s = 3;
            }
        }
        if (typeof(type) == 'function') { // 如果传来msg,fun
            if (typeof(fun) == 'number') {
                s = fun;
            } else {
                s = 3;
            }
            fun = type;
            type = '';
        }
        if (typeof(type) == 'number') { // 如果传来msg,s
            s = type;
            type = '';
        }

        $('.dialog_overlay').before(
            '<div class="dialog_content">' +
            '<style>.dialog_content{width: 260px; /*height:160px;*/ margin-top: -80px; margin-left: -130px; transform: scale(0)}' +
            '.dialog_content .title{display:block; padding: 0 10px 0 20px; height: 42px; line-height: 42px; font-size: 14px; overflow: hidden; border-radius: 2px 2px 0 0; background-color: #F8F8F8; color: #333;}' +
            '.dialog_content .title span{display: block; float: right; font-size: 20px;padding-right: 5px; cursor: pointer;}' +
            '.dialog_content .tip_content{font-size:14px; margin:20px; line-height:32px;' + pop.isHasIcon(type) + '}' +
            '.dialog_content .count_down_tip{display: block; padding-left:20px; position: absolute; bottom: 20px; color: #666}' +
            '</style>' +
            '<h3 class="title"><b>提示：</b><span class="close_btn">x</span></h3>' +
            '<p class="tip_content">' + msg + '</p>' +
            '<p class="count_down_tip">提示窗口将在 <b>' + s + '</b> s后关闭！</p>' +
            '</div>'
        );
        pop.alertDialogAnimate('show');
        pop.clickDialogOverlay('alert');
        pop.clickCloseBtn('alert');
        var t = setInterval(function () { // 倒计时
            s--;
            $('.count_down_tip>b').text(s);
            if (s <= 0) {
                clearInterval(t);
                t = null;
                pop.alertDialogAnimate('hide');
                fun && fun(); // 倒计时结束启动传进来的后续执行函数
            }
        }, 1000);
    },
    confirm: function (msg, fun) { // confirm弹窗
        pop.appendDialog();
        $('.dialog_overlay').before(
            '<div class="dialog_content">' +
            '<style>.dialog_content{width: 260px; /*height:160px;*/ margin-top:-80px; margin-left: -130px; transform:rotateY(90deg);}' +
            '.dialog_content .title{display: block; padding: 0 10px 0 20px; height: 42px; line-height: 42px; border-bottom: 1px solid #eee; font-size: 14px; color: #333; overflow: hidden; background-color: #F8F8F8; border-radius: 2px 2px 0 0;}' +
            '.dialog_content .title .close_btn{display: block; float: right; font-size: 20px;padding-right: 5px; cursor: pointer;}' +
            '.dialog_content .tip_content{position: relative; padding: 20px; line-height: 24px; word-break: break-all; overflow: hidden; font-size: 14px; overflow-x: hidden; overflow-y: auto;}' +
            '.dialog_content .confirm_btn_wrap{display: block; text-align: right; padding: 0 10px 12px; pointer-events: auto; user-select: none; -webkit-user-select: none;}' +
            '.dialog_content .confirm_btn_wrap button{display: inline-block; vertical-align: top; height: 28px; line-height: 28px; margin: 6px 6px 0; padding: 0 15px; border: 1px solid #dedede; background-color: #f1f1f1; color: #333; border-radius: 2px; font-weight: 400; cursor: pointer; text-decoration: none;}' +
            '.dialog_content .confirm_btn_wrap button.sure_btn{border-color: #4898d5; background-color: #2e8ded; color: #fff;}' +

            '</style>' +
            '<div class="title"><b>操作提示：</b><span class="close_btn">x</span></div>' +
            '<h3 class="tip_content">' + msg + '</h3>' +
            '<div class="confirm_btn_wrap"><button class="sure_btn">确定</button><button class="cancel_btn">取消</button></div>' +
            '</div>'
        );
        pop.confirmDialogAnimate('show');
        pop.clickDialogOverlay('confirm');
        pop.clickCloseBtn('confirm');
        pop.clickCancelBtn('confirm');
        pop.clickSureBtn('confirm', fun);
    },
    open: function (msg, type, fun) { // open弹窗
        pop.appendDialog();
        if (typeof(type) == 'function') { // 判断如果type传来的是函数，也就是type位置被fun占用
            fun = type; // 将type的值赋给fun
            type = ''; // type值清空
        }
        $('.dialog_overlay').before(
            '<div class="dialog_content">' +
            '<style>.dialog_content{width: 320px; height:180px; margin-top:-90px; margin-left: -160px; transform:rotateX(90deg);}' +
            '.dialog_content .title{display: block; background: #f6f6f6; color: #212a31; font-size: 16px; font-weight: 700; height: 46px; line-height: 46px; border-bottom: 1px solid #D5D5D5; border-radius: 2px 2px 0 0; padding: 0 10px 0 20px;}' +
            '.dialog_content .title .close_btn{display: block; float: right; font-size: 20px;padding-right: 5px; cursor: pointer;}' +
            '.dialog_content .tip_content{position: relative; margin: 20px; line-height: 32px; word-break: break-all; overflow: hidden; font-size: 14px; overflow-x: hidden; overflow-y: auto;' + pop.isHasIcon(type) + '}' +
            '.dialog_content .confirm_btn_wrap{display: block;padding: 0 10px 12px; pointer-events: auto; user-select: none; -webkit-user-select: none; text-align: center; padding-top: 15px; padding-bottom: 15px; background: #f0f4f7; border-top: 1px #c7c7c7 solid;}' +
            '.dialog_content .confirm_btn_wrap button{font-family:"microsoft yahei"; display: inline-block; vertical-align: top; height: 28px; line-height: 28px; margin: 6px 6px 0; padding: 0 15px; border: 1px solid #dedede; background-color: #f1f1f1; color: #333; border-radius: 2px; font-weight: 400; cursor: pointer; text-decoration: none;}' +
            '.dialog_content .confirm_btn_wrap button.sure_btn{border-color: #4898d5; background-color: #0071ce; color: #fff;}' +
            '.dialog_content .confirm_btn_wrap button.sure_btn:hover{background-color: #2e8ded;}' +
            '</style>' +
            '<div class="title"><b>温馨提示：</b><span class="close_btn">x</span></div>' +
            '<h3 class="tip_content">' + msg + '</h3>' +
            '<div class="confirm_btn_wrap"><button class="sure_btn">确定</button></div>' +
            '</div>'
        );
        pop.openDialogAnimate('show');
        pop.clickDialogOverlay('open');
        pop.clickCloseBtn('open');
        pop.clickSureBtn('open', fun);
    },
    prompt: function (msg, target, time) {
        if (typeof(target) == 'number') {
            if (time) {
                var bus = time;
                time = target;
                target = bus;
            } else {
                time = target;
                target = '';
            }
        }
        if (target) { // 如果指定了目标父元素
            var obj = $(this.matchingSelector(target));
            var position = obj.css('position');
            if (position != 'relative' && position != 'absolute' && position != 'fixed' && position != 'sticky') {
                obj.addClass('parentPosition');
            }
        } else {
            var obj = $('body');
        }
        var newPrompt =
            '<div id="new_prompt">' +
            '<style type="text/css">' +
            '*{margin:0; padding: 0;}' +
            '#new_prompt{position:fixed;z-index: 123456789; left: 50%;top: 50%; transform: translate(-50%,-50%); background: rgba(0,0,0,0.8); border-radius: 5px; overflow: hidden; opacity: 0;}' +
            '#new_prompt p{padding: 8px 10px; color: #fff; font-size: 14px; margin: 0;}' +
            '.parentPosition{position: relative;}' +
            '</style>' +
            '<p>' + msg + '</p>' +
            '</div>'
        if ($('#new_prompt').length == 0) {
            obj.append(newPrompt);
            var showTime = (time ? time : 1500);
            $('#new_prompt').animate({'opacity': '1'}, 500, function () {
                setTimeout(function () {
                    $('#new_prompt').animate({'opacity': '0'}, 500, function () {
                        $('#new_prompt').remove();
                        obj.removeClass('parentPosition');
                    });
                }, showTime);
            });
        }
    },
    matchingSelector: function (selector) { // 传进来的nav元素盒子进行选择器适配
        selector = $.trim(selector);
        if (selector.slice(0, 1) == '.' || selector.slice(0, 1) == '#') {
            selector = selector;
        } else if ($('#' + selector).length > 0) {
            selector = '#' + selector;
        } else if ($('.' + selector).length > 0) {
            selector = '.' + selector;
        }
        return selector;
    }
};