# LiteBanner 一款超级好看类似画廊的Banner

示列
lBannerView.setPageDatas(bannerList, testBannerAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "点击：" + position, Toast.LENGTH_LONG).show();
                    }
                })
                .start();

效果图如下
<img src="https://raw.githubusercontent.com/yixi195/LiteBanner/master/app/screenshot/preview.gif" width="240">
